<template>
    <v-container>
        <v-snackbar
            v-model="snackbar.status"
            :timeout="snackbar.timeout"
            :color="snackbar.color"
        >
            
            <v-btn style="margin-left: 80px;" text @click="snackbar.status = false">
                Close
            </v-btn>
        </v-snackbar>
        <div class="panel">
            <div class="gs-bundle-of-buttons" style="max-height:10vh;">
                <v-btn @click="addNewRow" @class="contrast-primary-text" small color="primary">
                    <v-icon small style="margin-left: -5px;">mdi-plus</v-icon>등록
                </v-btn>
                <v-btn :disabled="!selectedRow" style="margin-left: 5px;" @click="openEditDialog()" class="contrast-primary-text" small color="primary">
                    <v-icon small>mdi-pencil</v-icon>수정
                </v-btn>
                <v-btn style="margin-left: 5px;" @click="registerUserDialog = true" class="contrast-primary-text" small color="primary" :disabled="!hasRole('User')">
                    <v-icon small>mdi-minus-circle-outline</v-icon>RegisterUser
                </v-btn>
                <v-dialog v-model="registerUserDialog" width="500">
                    <RegisterUser
                        @closeDialog="registerUserDialog = false"
                        @registerUser="registerUser"
                    ></RegisterUser>
                </v-dialog>
                <v-btn :disabled="!selectedRow" style="margin-left: 5px;" @click="applyForAuthorshipDialog = true" class="contrast-primary-text" small color="primary" :disabled="!hasRole('User')">
                    <v-icon small>mdi-minus-circle-outline</v-icon>ApplyForAuthorship
                </v-btn>
                <v-dialog v-model="applyForAuthorshipDialog" width="500">
                    <ApplyForAuthorship
                        @closeDialog="applyForAuthorshipDialog = false"
                        @applyForAuthorship="applyForAuthorship"
                    ></ApplyForAuthorship>
                </v-dialog>
                <v-btn :disabled="!selectedRow" style="margin-left: 5px;" @click="declineApplicationDialog = true" class="contrast-primary-text" small color="primary" :disabled="!hasRole('Admin')">
                    <v-icon small>mdi-minus-circle-outline</v-icon>DeclineApplication
                </v-btn>
                <v-dialog v-model="declineApplicationDialog" width="500">
                    <DeclineApplication
                        @closeDialog="declineApplicationDialog = false"
                        @declineApplication="declineApplication"
                    ></DeclineApplication>
                </v-dialog>
                <v-btn :disabled="!selectedRow" style="margin-left: 5px;" @click="startSubscribe" class="contrast-primary-text" small color="primary" :disabled="!hasRole('User')">
                    <v-icon small>mdi-minus-circle-outline</v-icon>StartSubscribe
                </v-btn>
                <v-btn :disabled="!selectedRow" style="margin-left: 5px;" @click="accpetApplicationDialog = true" class="contrast-primary-text" small color="primary" :disabled="!hasRole('Admin')">
                    <v-icon small>mdi-minus-circle-outline</v-icon>AccpetApplication
                </v-btn>
                <v-dialog v-model="accpetApplicationDialog" width="500">
                    <AccpetApplication
                        @closeDialog="accpetApplicationDialog = false"
                        @accpetApplication="accpetApplication"
                    ></AccpetApplication>
                </v-dialog>
            </div>
            <div class="mb-5 text-lg font-bold"></div>
            <div class="table-responsive">
                <v-table>
                    <thead>
                        <tr>
                        <th>Id</th>
                        <th>Nickname</th>
                        <th>AuthorshipStatus</th>
                        <th>CreatedAt</th>
                        <th>Subscriber</th>
                        <th>AccountId</th>
                        <th>Password</th>
                        <th>AuthorsProfile</th>
                        <th>Autornickname</th>
                        <th>AgreedToMarketing</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(val, idx) in value" 
                            @click="changeSelectedRow(val)"
                            :key="val"  
                            :style="val === selectedRow ? 'background-color: rgb(var(--v-theme-primary), 0.2) !important;':''"
                        >
                            <td class="font-semibold">{{ idx + 1 }}</td>
                            <td class="whitespace-nowrap" label="Nickname">{{ val.nickname }}</td>
                            <td class="whitespace-nowrap" label="AuthorshipStatus">{{ val.authorshipStatus }}</td>
                            <td class="whitespace-nowrap" label="CreatedAt">{{ val.createdAt }}</td>
                            <td class="whitespace-nowrap" label="Subscriber">{{ val.subscriber }}</td>
                            <td class="whitespace-nowrap" label="AccountId">{{ val.accountId }}</td>
                            <td class="whitespace-nowrap" label="Password">{{ val.password }}</td>
                            <td class="whitespace-nowrap" label="AuthorsProfile">{{ val.authorsProfile }}</td>
                            <td class="whitespace-nowrap" label="Autornickname">{{ val.autornickname }}</td>
                            <td class="whitespace-nowrap" label="AgreedToMarketing">{{ val.agreedToMarketing }}</td>
                            <v-row class="ma-0 pa-4 align-center">
                                <v-spacer></v-spacer>
                                <Icon style="cursor: pointer;" icon="mi:delete" @click="deleteRow(val)" />
                            </v-row>
                        </tr>
                    </tbody>
                </v-table>
            </div>
        </div>
        <v-col>
            <v-dialog
                v-model="openDialog"
                transition="dialog-bottom-transition"
                width="35%"
            >
                <v-card>
                    <v-toolbar
                        color="primary"
                        class="elevation-0 pa-4"
                        height="50px"
                    >
                        <div style="color:white; font-size:17px; font-weight:700;">Users 등록</div>
                        <v-spacer></v-spacer>
                        <v-icon
                            color="white"
                            small
                            @click="closeDialog()"
                        >mdi-close</v-icon>
                    </v-toolbar>
                    <v-card-text>
                        <Users :offline="offline"
                            :isNew="!value.idx"
                            :editMode="true"
                            :inList="false"
                            v-model="newValue"
                            @add="append"
                        />
                    </v-card-text>
                </v-card>
            </v-dialog>
            <v-dialog
                v-model="editDialog"
                transition="dialog-bottom-transition"
                width="35%"
            >
                <v-card>
                    <v-toolbar
                        color="primary"
                        class="elevation-0 pa-4"
                        height="50px"
                    >
                        <div style="color:white; font-size:17px; font-weight:700;">Users 수정</div>
                        <v-spacer></v-spacer>
                        <v-icon
                            color="white"
                            small
                            @click="closeDialog()"
                        >mdi-close</v-icon>
                    </v-toolbar>
                    <v-card-text>
                        <div>
                            <String label="Nickname" v-model="selectedRow.nickname" :editMode="true"/>
                            <String label="AuthorshipStatus" v-model="selectedRow.authorshipStatus" :editMode="true"/>
                            <Date label="CreatedAt" v-model="selectedRow.createdAt" :editMode="true"/>
                            <Boolean label="Subscriber" v-model="selectedRow.subscriber" :editMode="true"/>
                            <String label="AccountId" v-model="selectedRow.accountId" :editMode="true"/>
                            <String label="Password" v-model="selectedRow.password" :editMode="true"/>
                            <String label="AuthorsProfile" v-model="selectedRow.authorsProfile" :editMode="true"/>
                            <String label="Autornickname" v-model="selectedRow.autornickname" :editMode="true"/>
                            <Boolean label="AgreedToMarketing" v-model="selectedRow.agreedToMarketing" :editMode="true"/>
                            <v-divider class="border-opacity-100 my-divider"></v-divider>
                            <v-layout row justify-end>
                                <v-btn
                                    width="64px"
                                    color="primary"
                                    @click="save"
                                >
                                    수정
                                </v-btn>
                            </v-layout>
                        </div>
                    </v-card-text>
                </v-card>
            </v-dialog>
        </v-col>
    </v-container>
</template>

<script>
import { ref } from 'vue';
import { useTheme } from 'vuetify';
import BaseGrid from '../base-ui/BaseGrid.vue'


export default {
    name: 'usersGrid',
    mixins:[BaseGrid],
    components:{
    },
    data: () => ({
        path: 'users',
        registerUserDialog: false,
        applyForAuthorshipDialog: false,
        declineApplicationDialog: false,
        accpetApplicationDialog: false,
    }),
    watch: {
    },
    methods:{
        async registerUser(params){
            try{
                var path = "registerUser".toLowerCase();
                var temp = await this.repository.invoke(this.selectedRow, path, params)
                // 스넥바 관련 수정 필요
                // this.$EventBus.$emit('show-success','RegisterUser 성공적으로 처리되었습니다.')
                for(var i = 0; i< this.value.length; i++){
                    if(this.value[i] == this.selectedRow){
                        this.value[i] = temp.data
                    }
                }
                this.registerUserDialog = false
            }catch(e){
                console.log(e)
            }
        },
        async applyForAuthorship(params){
            try{
                var path = "applyForAuthorship".toLowerCase();
                var temp = await this.repository.invoke(this.selectedRow, path, params)
                // 스넥바 관련 수정 필요
                // this.$EventBus.$emit('show-success','ApplyForAuthorship 성공적으로 처리되었습니다.')
                for(var i = 0; i< this.value.length; i++){
                    if(this.value[i] == this.selectedRow){
                        this.value[i] = temp.data
                    }
                }
                this.applyForAuthorshipDialog = false
            }catch(e){
                console.log(e)
            }
        },
        async declineApplication(params){
            try{
                var path = "declineApplication".toLowerCase();
                var temp = await this.repository.invoke(this.selectedRow, path, params)
                // 스넥바 관련 수정 필요
                // this.$EventBus.$emit('show-success','DeclineApplication 성공적으로 처리되었습니다.')
                for(var i = 0; i< this.value.length; i++){
                    if(this.value[i] == this.selectedRow){
                        this.value[i] = temp.data
                    }
                }
                this.declineApplicationDialog = false
            }catch(e){
                console.log(e)
            }
        },
        async startSubscribe(){
            try{
                var path = "startSubscribe".toLowerCase();
                var temp = await this.repository.invoke(this.selectedRow, path, null)
                // 스넥바 관련 수정 필요
                // this.$EventBus.$emit('show-success','StartSubscribe 성공적으로 처리되었습니다.')
                for(var i = 0; i< this.value.length; i++){
                    if(this.value[i] == this.selectedRow){
                        this.value[i] = temp.data
                    }
                }
            }catch(e){
                console.log(e)
            }
        },
        async accpetApplication(params){
            try{
                var path = "accpetApplication".toLowerCase();
                var temp = await this.repository.invoke(this.selectedRow, path, params)
                // 스넥바 관련 수정 필요
                // this.$EventBus.$emit('show-success','AccpetApplication 성공적으로 처리되었습니다.')
                for(var i = 0; i< this.value.length; i++){
                    if(this.value[i] == this.selectedRow){
                        this.value[i] = temp.data
                    }
                }
                this.accpetApplicationDialog = false
            }catch(e){
                console.log(e)
            }
        },
    }
}

</script>